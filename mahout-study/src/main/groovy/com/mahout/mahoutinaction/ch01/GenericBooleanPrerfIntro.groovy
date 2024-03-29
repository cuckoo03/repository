package com.mahout.mahoutinaction.ch01

import groovy.transform.TypeChecked

import java.nio.file.FileSystems
import java.nio.file.Path

import org.apache.mahout.cf.taste.eval.DataModelBuilder
import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator
import org.apache.mahout.cf.taste.impl.common.FastByIDMap
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator
import org.apache.mahout.cf.taste.impl.model.GenericBooleanPrefDataModel
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.LogLikelihoodSimilarity
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.model.PreferenceArray
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.similarity.UserSimilarity

@TypeChecked
class GenericBooleanPrerfIntro {
	static main(args) {
		def p = FileSystems.getDefault().getPath(
				"C:/Users/cuckoo03/workspace/mahout-study/target/classes/resources/ua.base")
		def dataModel = new GenericBooleanPrefDataModel(
				GenericBooleanPrefDataModel.toDataMap(new FileDataModel(
				p.toFile())))

		def eval = new AverageAbsoluteDifferenceRecommenderEvaluator()

		def builder = { DataModel model ->
			def similar = new LogLikelihoodSimilarity(model)
			def neighbor = new NearestNUserNeighborhood(
					10, similar, model)
			
			return new GenericUserBasedRecommender(model, neighbor, similar)
		}

		def modelBuilder = { FastByIDMap<PreferenceArray> trainigData ->
			return new GenericBooleanPrefDataModel(
					GenericBooleanPrefDataModel.toDataMap(trainigData))
		}

		def score = eval.evaluate(builder, modelBuilder, dataModel, 0.9, 1.0)
		println score
	}
}