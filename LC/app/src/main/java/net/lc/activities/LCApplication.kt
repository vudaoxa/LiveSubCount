package net.lc.activities

import android.support.multidex.MultiDexApplication
import com.facebook.drawee.backends.pipeline.Fresco
import com.joanzapata.iconify.Iconify
import com.joanzapata.iconify.fonts.*
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * Created by HP on 11/25/2016.
 */

class LCApplication : MultiDexApplication() {
    override fun onCreate() {
        super.onCreate()
        initAwesome()
        Fresco.initialize(this)
        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        val realmConfig = RealmConfiguration.Builder()
                .name("lc.realm")
                .build()
//        Realm.deleteRealm(realmConfig)
        Realm.setDefaultConfiguration(realmConfig)

//        val realmConfig = RealmConfiguration.Builder(
//                this).deleteRealmIfMigrationNeeded().build()
//        Realm.setDefaultConfiguration(realmConfig)
    }
    private fun initAwesome() {
        Iconify
                .with(FontAwesomeModule())
                .with(EntypoModule())
                .with(TypiconsModule())
                .with(MaterialModule())
                .with(MaterialCommunityModule())
                .with(MeteoconsModule())
                .with(WeathericonsModule())
                .with(SimpleLineIconsModule())
                .with(IoniconsModule())
    }
}
