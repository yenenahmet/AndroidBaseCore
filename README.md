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
		// or //
		implementation 'com.github.yenenahmet.AndroidBaseCore:basecorelibrary:version'
    		implementation 'com.github.yenenahmet.AndroidBaseCore:base-exoplayer:version'
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
  
  
  # 1. Project Guidelines
  
  
  ## 1.1 Class Files
  
  Class names are written in [UpperCamelCase](http://en.wikipedia.org/wiki/CamelCase).
  
  The name of the class should end with the name of the component; for example: `MainActivity`, `MainFragment`, `CouponManager`, `DialogUtils`.
  
  
  ## 1.2 Resource Files
  
  Resource files names are written in [snake_case](https://en.wikipedia.org/wiki/Snake_case).
  
  Naming conventions for drawables:
  
  | Asset Type   | Prefix            |		Example               |
  |--------------| ------------------|-----------------------------|
  | Action bar   | `ab_`             | `ab_stacked.9.png`          |
  | Button       | `btn_`	            | `btn_send_pressed.9.png`    |
  | Dialog       | `dialog_`         | `dialog_top.9.png`          |
  | Divider      | `divider_`        | `divider_horizontal.9.png`  |
  | Icon         | `ic_`	            | `ic_star.png`               |
  | Menu         | `menu_	`           | `menu_submenu_bg.9.png`     |
  | Notification | `notification_`	| `notification_bg.9.png`     |
  | Tabs         | `tab_`            | `tab_pressed.9.png`         |
  
  Naming conventions for icons:
  
  | Asset Type                      | Prefix             | Example                      |
  | --------------------------------| ----------------   | ---------------------------- |
  | Icons                           | `ic_`              | `ic_star.png`                |
  | Launcher icons                  | `ic_launcher`      | `ic_launcher_calendar.png`   |
  | Menu icons and Action Bar icons | `ic_menu`          | `ic_menu_archive.png`        |
  | Status bar icons                | `ic_stat_notify`   | `ic_stat_notify_msg.png`     |
  | Tab icons                       | `ic_tab`           | `ic_tab_recent.png`          |
  | Dialog icons                    | `ic_dialog`        | `ic_dialog_info.png`         |
  
  Naming conventions for selector states:
  
  | State	       | Suffix          | Example                     |
  |--------------|-----------------|-----------------------------|
  | Normal       | `_normal`       | `btn_order_normal.9.png`    |
  | Pressed      | `_pressed`      | `btn_order_pressed.9.png`   |
  | Focused      | `_focused`      | `btn_order_focused.9.png`   |
  | Disabled     | `_disabled`     | `btn_order_disabled.9.png`  |
  | Selected     | `_selected`     | `btn_order_selected.9.png`  |
  
  
  ## 1.3 Layout files
  
  Layout files should match the name of the Android components that they are intended for but moving the top level component name to the beginning. For example, if we are creating a layout for the `MainActivity`, the name of the layout file should be `activity_main.xml`.
  
  | Component        | Class Name             | Layout Name                   |
  | ---------------- | ---------------------- | ----------------------------- |
  | Activity         | `UserProfileActivity`  | `activity_user_profile.xml`   |
  | Fragment         | `SignUpFragment`       | `fragment_sign_up.xml`        |
  | Dialog           | `ChangePasswordDialog` | `dialog_change_password.xml`  |
  | AdapterView item | ---                    | `item_person.xml`             |
  | Partial layout   | ---                    | `partial_stats_bar.xml`       |
  
  
  ## 1.4 Parameter Files
  
  Parameter names are written in [camelCase](http://en.wikipedia.org/wiki/CamelCase).
  
  For example: `userId`, `bettingPhase`, `isTopEvent`, `hasAnyTopEvent`.
  
  
  ## 1.5 Ids of Views
  
  Id names are written in [snake_case](http://en.wikipedia.org/wiki/Snake_case).
  
  The name of the id should start with the name of the view; for example: `button_save`, `image_view_arrow`, `relative_layout_avatar`, `edit_text_name`.
  
  
  ## 1.6 Branch Names
  
  Branch names are written in [kebab_case](http://en.wikipedia.org/wiki/Kebab_case).
  
  The names of the branch should start with the id of the Jira task; for example: `fix/ANDROID-1552-TopEvent`, `feature/ANDROID-1555-ColoredSquadList`.
  
  
  ## 1.7 Commit Messages
  
  Commit messages are written in a special name convention([kebab-case] *message).
  
  The messages of the commit should start with the name of the branch with square brackets; for example: `[fix/ANDROID-1552-TopEvent] *Add top event icons`, `[feature/ANDROID-1555-ColoredSquadList] *Make implementation colored squad list`.
  

