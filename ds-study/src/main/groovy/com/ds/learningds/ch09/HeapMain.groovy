package com.ds.learningds.ch09

class HeapMain {
	static main(args) {
		int n, item
		Heap h = new Heap()
		h.insertHeap(13)
		h.insertHeap(8)
		h.insertHeap(10)
		h.insertHeap(15)
		h.insertHeap(20)
		h.insertHeap(19)

		h.printHeap()

		n = h.getHeapSize()
		for (int i = 1; i <= n; i++) {
			item = h.deleteHeap()
			println "delete item: $item"
		}
	}
}
class Heap {
	int heapSize
	int[] itemHeap
	Heap() {
		heapSize = 0
		itemHeap = new int[50]
	}
	void insertHeap(int item) {
		int i = ++heapSize
		while ((i != 1) && (item > itemHeap[i/2])) {
			itemHeap[i] = itemHeap[i/2]
			i/=2
		}
		itemHeap[i] = item
	}
	int getHeapSize() {
		return this.heapSize
	}
	int deleteHeap() {
		int parent, child
		int item, temp
		item = itemHeap[1]
		temp = itemHeap[heapSize--]
		parent = 1
		child = 2

		while (child <= heapSize) {
			if ((child < heapSize) && (itemHeap[child] < itemHeap[child+1])) {
				child++
			}
			if (temp >= itemHeap[child]) {
				break
			}
			itemHeap[parent] = itemHeap[child]
			parent = child
			child *= 2
		}
		itemHeap[parent] = temp
		return item
	}
	void printHeap() {
		print "Heap:"
		for (int i = 0; i < heapSize; i++) {
			print " " + itemHeap[i]
		}
	}
}
