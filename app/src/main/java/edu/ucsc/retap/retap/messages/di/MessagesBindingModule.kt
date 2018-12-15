package edu.ucsc.retap.retap.messages.di

import android.app.Activity
import android.content.Context
import dagger.Binds
import dagger.Module
import edu.ucsc.retap.retap.common.di.ActivityScope

/**
 * Bindings for MessagesComponent.
 */
@Module
abstract class MessagesBindingModule {

    @Binds
    @ActivityScope
    abstract fun bindContext(activity: Activity): Context
}
