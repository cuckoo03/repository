package com.gfpij.ch04

import groovy.transform.TypeChecked

@TypeChecked
class Asset {
	enum AssetType {BOND, STOCK}
	final AssetType type
	final int value
	Asset(AssetType assetType, int assetValue) {
		this.type = assetType
		this.value = assetValue
	}
}
