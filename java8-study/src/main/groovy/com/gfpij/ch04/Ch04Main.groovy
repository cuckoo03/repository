package com.gfpij.ch04

import java.util.concurrent.locks.ReentrantLock
import java.util.function.Consumer
import java.util.function.Function
import java.util.function.Predicate
import java.util.stream.Stream

import groovy.transform.TypeChecked

@TypeChecked
class Ch04Main {
	static void main(args) {
		def assets = [
			new Asset(Asset.AssetType.BOND, 1),
			new Asset(Asset.AssetType.STOCK, 2)
		]
		
		println totalAssetValues(assets)
		println totalAssetValues(assets, { asset -> true })
		println totalAssetValues(assets, { a -> a.type == Asset.AssetType.BOND })
		println totalAssetValues(assets, { a -> a.value == 2 })
		
		Stream.of("c://no1").map{ p -> new File(p).getCanonicalPath() }
			.forEach(System.out.&println)
		
		Stream.of("c://no1").map{ String p -> 
			try {
				return new File(p).getCanonicalPath()
			} catch (Exception e) {
				return e.message
			}
		}.forEach(System.out.&println)
	}
	static int totalAssetValues(List<Asset> assets) {
		return assets.stream().mapToInt(Asset.&getValue).sum()
	}
	static int totalAssetValues(List<Asset> assets, Predicate<Asset> assetSelector) {
		return assets.stream().filter(assetSelector).mapToInt(Asset.&getValue).sum()
	}
}
