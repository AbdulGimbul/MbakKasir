package network

//actual class RemoteConfigManager {
//    private val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
//
//    init {
//        val configSettings = remoteConfigSettings {
//            minimumFetchIntervalInSeconds = 3600
//        }
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
//    }
//
//    actual fun fetchBaseUrl(): String {
//        remoteConfig.fetchAndActivate()
//            .addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val updated = task.result
//                    println("Config params updated: $updated")
//                } else {
//                    println("Fetch failed")
//                }
//            }
//        return remoteConfig.getString("base_url")
//    }
//}