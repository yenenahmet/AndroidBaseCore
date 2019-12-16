# AndroidBaseCore

# Setup

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  

	dependencies {
	        implementation 'com.github.yenenahmet:AndroidBaseCore:version'
	}
  
  -----
  
  
 # base adapters
 
 BaseRecyclerViewAdapter ->  setItems(),setChanged(),getItem(),getItemPosition(),getItems(),addItem(),addItemNotNotify(),addRestoreItem(),addItems(),removeItem(),getInflater(),clearItems()
 
 
  Medium Post : https://medium.com/@ynnahmet/nedir-bu-base-classlar-abstract-class-lar-android-kulland%C4%B1%C4%9F%C4%B1m-base-class-lar-fa44133fa4dc
