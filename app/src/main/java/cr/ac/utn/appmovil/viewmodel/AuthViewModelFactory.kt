package cr.ac.utn.appmovil.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import cr.ac.utn.appmovil.repository.AuthRepository



/**
 * Factory compartida para instanciar LoginViewModel y RegisterViewModel.
 */
class AuthViewModelFactory(private val repository: AuthRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(repository) as T
            }
            /*modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(repository) as T
            }*/
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
