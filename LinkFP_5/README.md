# 📱 LinkFP - Entrega 5: Autenticación con Firebase y Jetpack Compose

Este documento contiene el resumen detallado de los cambios realizados para la quinta entrega del proyecto **LinkFP**, donde se implementan la autenticación de usuarios (Inicio de Sesión y Registro) utilizando **Firebase Authentication** y **Jetpack Compose** bajo una arquitectura **MVVM sin inyección de dependencias (Dagger Hilt)**.

---

## 📋 Resumen de Cambios Realizados

### 1. ⚙️ Configuración del Sistema de Construcción y Servicios de Google
* **`app/google-services.json`**: Creado e importado en la carpeta `app/` para vincular el paquete `es.alan_turing.linkfp`.
* **`app/build.gradle.kts`**: Se ha aplicado el complemento `alias(libs.plugins.google.gms.google.services)` e incluido la dependencia de `implementation(libs.firebase.auth)`.
* **`build.gradle.kts` (Proyecto)**: Configurado el complemento `alias(libs.plugins.google.gms.google.services) apply false`.
* **`gradle/libs.versions.toml`**: Añadidas y organizadas las versiones de `googleGmsGoogleServices` ("4.5.0"), `firebaseAuth` ("25.0.0"), `firebase-bom`, `firebase-auth` y el complemento de servicios de Google.

---

### 2. 🧠 Capa ViewModel (`viewmodel/AuthViewModel.kt`)
* Creada la clase `AuthViewModel` extendiendo de `ViewModel` **sin utilizar Dagger Hilt** (instanciando la referencia de `FirebaseAuth.getInstance()` directamente).
* **Gestión de Estados Reactivos**:
  * `currentUser`: Expone un `StateFlow<FirebaseUser?>` que notifica dinámicamente si hay una sesión iniciada y sus datos (como el correo del usuario).
  * `authState`: Expone un `StateFlow<AuthState>` con la jerarquía `Idle`, `Loading`, `Success` y `Error`.
* **Métodos principales**:
  * `login(email, password, onSuccess, onError)`: Autentica credenciales en Firebase Auth.
  * `signUp(email, password, onSuccess, onError)`: Registra un nuevo usuario en Firebase Auth.
  * `logout()`: Cierra la sesión activa en el dispositivo.
  * `clearError()`: Reinicia el estado a `Idle`.
* **Documentación & TODOs**: Documentado exhaustivamente con KDoc explicando la arquitectura MVVM y comentarios `// TODO` aclaratorios indicando la futura vinculación final con Firebase Console.

---

### 3. 🎨 Capa de Interfaz de Usuario (`ui/auth/`)
* **`es.alan_turing.linkfp.ui.auth.InicioSesion.kt`**:
  * Formulario personalizado de inicio de sesión con campos para Correo Electrónico y Contraseña.
  * Botón para mostrar u ocultar la contraseña mediante iconos de Material 3 (`Visibility` / `VisibilityOff`).
  * Indicador de carga animado (`CircularProgressIndicator`) y tarjetas de error legibles.
  * Enlace `TextButton` para navegar hacia la pantalla de Registro y botón `Volver`.
* **`es.alan_turing.linkfp.ui.auth.Registro.kt`**:
  * Formulario de alta de usuario con campos de Correo, Contraseña y Confirmar Contraseña.
  * Validaciones locales previas al envío: campos vacíos, coincidencia de contraseñas y longitud mínima de 6 caracteres.
  * Enlace `TextButton` para navegar hacia Inicio de Sesión y botón `Volver`.

---

### 4. 🧭 Pantalla Principal y Navegación (`Principal.kt` y `NavegacionApp.kt`)
* **`RutasNavegacion.kt`**: Añadidos los nodos `data object InicioSesion` y `data object Registro`.
* **`Principal.kt`**:
  * **Visualización de Usuario (Text)**: Incluye una tarjeta superior que muestra el estado de la sesión activa (`Usuario: correo@ejemplo.com` o `Sesión no iniciada`).
  * **Botón de Acción (TextButton)**: Permite iniciar sesión (redirigiendo a `InicioSesion`) cuando no hay sesión activa, o bien cerrarla (`authViewModel.logout()`) cuando sí la hay.
  * Nueva opción en el menú principal en forma de `MenuItemCard` para ir a Inicio de Sesión / Perfil.
* **`NavegacionApp.kt`**: Instancia y comparte `AuthViewModel` a lo largo del `NavHost`, registrando las rutas `RutasNavegacion.InicioSesion.route` y `RutasNavegacion.Registro.route`.

---

### 5. 🌍 Internacionalización y Estilos (Traducción, Colores y Formas)
* **`res/values/strings.xml`** (Castellano) y **`res/values-en/strings.xml`** (Inglés): Definidas todas las cadenas de texto del flujo de inicio de sesión, registro, validaciones y estados de usuario en ambos idiomas.
* Respetados los estilos del tema (`MaterialTheme.colorScheme`, `CardDefaults`, botones y tipografías).
