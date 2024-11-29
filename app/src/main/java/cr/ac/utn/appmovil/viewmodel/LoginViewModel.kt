package cr.ac.utn.appmovil.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cr.ac.utn.appmovil.model.LoginResponse
import cr.ac.utn.appmovil.repository.AuthRepository
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * ViewModel para manejar el flujo de login.
 */
class LoginViewModel(private val repository: AuthRepository) : ViewModel() {

    private val _loginResult = MutableLiveData<Response<LoginResponse>>()
    val loginResult: LiveData<Response<LoginResponse>> get() = _loginResult

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    /**
     * Realiza el login de un técnico enviando las credenciales.
     * @param id ID del técnico.
     * @param password Contraseña del técnico.
     */
    fun login(id: String, password: String) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.login(id, password)
                if (response.isSuccessful) {
                    _loginResult.postValue(response)
                } else {
                    _errorMessage.postValue("Error: ${response.message()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Error de conexión: ${e.localizedMessage}")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
