package com.codetron.todoapp.di.component

import android.content.Context
import com.codetron.todoapp.di.module.RepositoryModule
import com.codetron.todoapp.ui.addupdate.AddUpdateFragment
import com.codetron.todoapp.ui.list.ListFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [RepositoryModule::class]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(fragment: ListFragment)
    fun inject(fragment:AddUpdateFragment)

}