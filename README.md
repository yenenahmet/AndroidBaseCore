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
  
 * To facilitate structures that can be used throughout the project
 
 # Base Adapters 
  1) BaseRecyclerViewAdapter
  2) BaseViewBindingRecyclerViewAdapter
  3) BaseRecyclerViewFilterAdapter
  4) BaseViewBindingRecyclerViewFilterAdapter
  5) BaseViewBindingRecyclerViewInputAdapter
  6) BaseSpinnerAdapter
  7) BaseViewBindingPagerAdapter
  
  # Alarm
  1) AlarmManagerManagement // Helper Class
  2) BaseAlarmReceiver 
  
  # Bindings 
  1) AdapterBindings
  
  # CustomViews 
  1) CreditCardCardNumberAppCompatEditText
  2) CreditCardDateAppCompatEditText
  3) CurrencyEditText
  4) CurrencyInputWatcher
  5) UnderLinedAppCompatTextView
  
  # Di (Dagger2)
  1) AppViewModelFactory 
  
  # Base Dialogs
  1) BaseBindingDialog 
  2) BaseDialog
  3) BindingPopupWindow
  
  # Filter
  1) BaseFilter
  
  # Listeners 
  1) PaginationScrollListener
  
  # Local
  1) LocaleManager  (Language)
  2) SharedPreferencesHelper (Stroge Helper)
  
  # Notification
  1) NotificationBuilderHelper
  
  # Remote
  1) DownloadFile 
  2) PageRxInputStream 
  
  # Security
  1) ChipperManger
  
  # Base Sms && Listeners
  1) BaseSmsReceiveActivity
  2) BaseSmsReceiveDaggerActivity
  3) BaseSmsReceiveDaggerLoadingFragment
  4) BaseSmsReceiveRemoteReadyLoadingActivity
  5) SmsListener
  
  # Base Ui's
  1) BaseActivity
  2) BaseDaggerActivity
  3) BaseDaggerFragment
  4) BaseDaggerLoadingActivity
  5) BaseDaggerLoadingFragment
  6) BaseFragment
  7) BaseLoadingActivity
  8) BaseLoadingFragment
  9) BaseRemoteActivity
  10) BaseRemoteFragment
  11) BaseRemoteReadyLoadingActivity
  12) BaseRemoteReadyLoadingFragment
  
  # Utils 
  1) ControlUtils 
  	-> Email Check
	-> Tc No Check
	-> Tax No Check
  2) FileUtils 
   	-> createPicturesFile
	-> takePicture
	-> saveBitmapToFile
  3) SizeUtils 
  	-> dpToPx
	-> pxToDp
	
# Base ViewModels	
  1) BaseRxSingleHandlerViewModel
  2) BaseRxViewModel
  3) BaseViewModel
  
# BaseApplication
 
 
  Medium Post :
  https://medium.com/@ynnahmet/nedir-bu-base-classlar-abstract-class-lar-android-kulland%C4%B1%C4%9F%C4%B1m-base-class-lar-fa44133fa4dc
