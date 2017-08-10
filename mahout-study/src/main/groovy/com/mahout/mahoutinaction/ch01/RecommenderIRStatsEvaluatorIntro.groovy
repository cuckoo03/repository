package com.mahout.mahoutinaction.ch01

import groovy.transform.TypeChecked;

import java.nio.file.FileSystems
import java.nio.file.Path

import org.apache.mahout.cf.taste.eval.IRStatistics
import org.apache.mahout.cf.taste.eval.RecommenderBuilder
import org.apache.mahout.cf.taste.eval.RecommenderIRStatsEvaluator
import org.apache.mahout.cf.taste.impl.eval.GenericRecommenderIRStatsEvaluator
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity
import org.apache.mahout.cf.taste.model.DataModel
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood
import org.apache.mahout.cf.taste.recommender.Recommender
import org.apache.mahout.cf.taste.similarity.UserSimilarity
import org.apache.mahout.common.RandomUtils

@TypeChecked
class RecommenderIRStatsEvaluatorIntro {
	static main(args) {
		def p = FileSystems.getDefault().getPath("C:/Users/cuckoo03/workspace/mahout-study/target/classes/resources/ua.base")
		def dataModel = new FileDataModel(p.toFile())

		RandomUtils.useTestSeed()

		def evaluator = new GenericRecommenderIRStatsEvaluator()
		def builder = { DataModel model ->
			def similarity =
					new PearsonCorrelationSimilarity(model)
			def neighborhood = new NearestNUserNeighborhood(
					2, similarity, model)
			return new GenericUserBasedRecommender(model, neighborhood, similarity)
		}

		def stats = evaluator.evaluate(builder, null, dataModel,
				null, 2, GenericRecommenderIRStatsEvaluator.CHOOSE_THRESHOLD, 1.0)
		
		println stats.getPrecision()
		println stats.getRecall()
	}
}
