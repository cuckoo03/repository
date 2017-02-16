package com.scala.pis.ch29

/**
 * @author cuckoo03
 */
object Chapter29 {
	def main(args: Array[String]) {
		val apple = SimpleDatabase.foodNamed("Apple").get
		println(apple)
		println(SimpleBrowser.recipesUsing(apple))
	}

	object Apple extends Food("Apple")
	object Orange extends Food("Orange")
	object FruitSalad extends Recipe("fruit salad", List(Apple, Orange), "")

	object SimpleDatabase extends Database with SimpleFoods with SimpleRecipes {
		//		def allFoods = List(Apple, Orange)
//		def allRecipes: List[Recipe] = List(FruitSalad)

		def foodNamed(name: String): Option[Food] = allFoods.find(_.name == name)

		private var categories = List(
			FoodCategory("fruits", List(Apple, Orange)),
			FoodCategory("misc", List(Apple)))

		//		def allCategories = categories
	}

	object SimpleBrowser extends Browser {
		val database = SimpleDatabase
	}

	abstract class Browser {
		val database: Database

		def recipesUsing(food: Food) =
			database.allRecipes.filter(recipe =>
				recipe.ingredients.contains(food))

		def displayCategory(category: database.FoodCategory) {
			println(category)
		}
	}

	abstract class Database extends FoodCategories {
		def allFoods: List[Food]
		def allRecipes: List[Recipe]

		def foodNames(name: String) =
			allFoods.find(f => f.name == name)

		//		case class FoodCategory(name: String, foods: List[Food])

		//		def allCategories: List[FoodCategory]
	}

	trait FoodCategories {
		case class FoodCategory(name: String, foods: List[Food])
		def allCategories: List[FoodCategory]
	}

	trait SimpleFoods {
		object Pear extends Food("Pear")
		def allFoods = List(Apple, Pear)
		def allCategories = Nil
	}

	trait SimpleRecipes {
		this: SimpleFoods =>

		object FruitSalad extends Recipe(
				"fruit salad", List(Apple, Pear), "Mix it")
		def allRecipes = List(FruitSalad)
	}
}