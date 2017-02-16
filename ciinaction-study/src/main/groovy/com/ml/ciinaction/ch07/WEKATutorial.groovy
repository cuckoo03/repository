package com.ml.ciinaction.ch07

import groovy.transform.TypeChecked
import weka.classifiers.Classifier
import weka.classifiers.Evaluation
import weka.classifiers.functions.RBFNetwork
import weka.core.Attribute
import weka.core.FastVector
import weka.core.Instance
import weka.core.Instances
import weka.experiment.LearningRateResultProducer

@TypeChecked
class WEKATutorial {
	private void executeWekaTutorial() {
		// 속성 생성
		def allAttributes = createAttributes()
		// 학습을 위한 데이터셋 생성
		def learningDataset = createLearningDataSet(allAttributes)
		// 예측 모델 성능 평가
		def predictiveModel = learnPredictiveModel(learningDataset)
		// 예측 모델 빌드
		def evaluation = evaluatePredictiveModel(predictiveModel,
				learningDataset)
		println evaluation.toSummaryString()
		// 새로운 데이터 예측 수행
		predictUnknownCases(learningDataset, predictiveModel)
	}

	private void predictUnknownCases(Instances learningDataSet,
			Classifier predictiveModel) {
		def testMaleInstance = createInstance(learningDataSet, 32, "male", 0)
		def testFemaleInstance = createInstance(learningDataSet, 32, "female", 0)
		def malePrediction = predictiveModel.classifyInstance(testMaleInstance)
		def femalePrediction = predictiveModel.classifyInstance(testFemaleInstance)
		println "Predicted number of logins[age=32]"
		println "Male=$malePrediction"
		println "Female=$femalePrediction"
	}

	// 성능 측정과 로그인 횟수를 예측한다
	private Evaluation evaluatePredictiveModel(Classifier classifier,
			Instances learningDataset) {
		def learningSetEvaluation = new Evaluation(learningDataset)
		learningSetEvaluation.evaluateModel(classifier, learningDataset)
		return learningSetEvaluation

	}

	private Classifier learnPredictiveModel(Instances learningDataset) {
		// 사용할 분류기를 생성
		def classifier = getClassifier()
		//학습 데이터셋을 사용한 예측 모델 빌드
		classifier.buildClassifier(learningDataset)
		return classifier
	}

	private Classifier getClassifier() {
		def rbfLearner = new RBFNetwork()
		//클러스터의 개수를 입력
		rbfLearner.setNumClusters(2)
		return rbfLearner
	}

	private Instances createLearningDataSet(FastVector allAttributes) {
		def trainingDataSet = new Instances("wekaTutorial", allAttributes, 4)
		trainingDataSet.setClassIndex(2)
		//예측할 속성을 지정
		addInstance(trainingDataSet, 20, "male", 5)
		addInstance(trainingDataSet, 30, "female", 2)
		addInstance(trainingDataSet, 40, "male", 3)
		addInstance(trainingDataSet, 35, "female", 4)
		return trainingDataSet
	}

	private void addInstance(Instances trainingDataSet, double age,
			String gender, int numLogins) {
		def  instance = createInstance(trainingDataSet, age, gender,
				numLogins)
		trainingDataSet.add(instance)
		
	}

	private Instance createInstance(Instances associatedDataSet, double age,
			String gender, int numLogins) {
		def instance = new Instance(3)
		instance.setDataset(associatedDataSet)
		instance.setValue(0, age)
		instance.setValue(1, gender)
		instance.setValue(2, numLogins)
		return instance
	}
	private FastVector createAttributes() {
		// 나이 속성
		def ageAttribute = new Attribute("age")

		def genderAttributeValues = new FastVector(2)
		genderAttributeValues.addElement("male")
		genderAttributeValues.addElement("female")
		// 성별을 표현하기 위한 명목형 속성 생성
		def genderAttribute = new Attribute("gender", genderAttributeValues)

		def numLoginAttribute = new Attribute("numLogins")

		//속성 저장을 위한 패스트벡터 생성
		def allAttributes = new FastVector(3)
		allAttributes.addElement(ageAttribute)
		allAttributes.addElement(genderAttribute)
		allAttributes.addElement(numLoginAttribute)
		return allAttributes
	}
	static main(args) {
		def wekaTut = new WEKATutorial()
		wekaTut.executeWekaTutorial()
	}
}
