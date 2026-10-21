# MPAZ Pañol: Sistema Móvil de Inventario, Reserva y Préstamo de Equipos

**Asignatura:** Desarrollo de Aplicaciones Móviles (DSY1105)  
**Institución:** Duoc UC - Escuela de Informática y Telecomunicaciones  
**Caso Formativo:** Escuela Escritora Marcela Paz (San Bernardo)

---

## 👥 Integrantes del Equipo
* **Vicente Vejar**
* **Gabriel lienlaf**

---

## 📱 Descripción del Proyecto (MVP)
**MPAZ Pañol** es una aplicación móvil desarrollada en Android con Jetpack Compose que digitaliza y centraliza el control de inventario, solicitudes de préstamo y seguimiento de equipos tecnológicos (proyectores, notebooks, kits de robótica LEGO WeDo 2.0 y placas ESP32) para la Escuela Marcela Paz.

---

## ✨ Requisitos Cumplidos
* **6 Composables / Pantallas Funcionales:** `SplashScreen`, `LoginScreen`, `RegisterScreen`, `CatalogoScreen`, `CarritoScreen` e `HistorialScreen`.
* **Patrón Arquitectónico MVVM:** Separación limpia de UI, capa de datos y estados con `ViewModel`.
* **Persistencia Local (SQLite / Room):** Guardado de usuarios y validaciones contra base de datos local.
* **Diseño Visual con Material Design 3:** Tarjetas elevadas, componentes `Scaffold` y paleta adaptada.
* **Regla de Negocio Crítica:** Bloqueo automatizado para solicitudes realizadas con menos de **3 horas de anticipación**.
* **Animaciones Material 3:** Transiciones dinámicas y visibilidad animada (`AnimatedVisibility`) para errores.
* **Recursos Nativos del Dispositivo:**
    1. *Efecto Háptico de Vibración* (`Vibrator` / `VibratorManager`).
    2. *Notificaciones Flotantes del Sistema* (`NotificationManagerCompat`).

---

## 📂 Estructura del Código

---

## 🚀 Instrucciones de Ejecución
1. Clonar el repositorio: `git clone https://github.com/vizh0/mpaz-panol-android.git`
2. Abrir el proyecto en **Android Studio**.
3. Sincronizar las dependencias de **Gradle**.
4. Seleccionar un emulador virtual (Android 8.0 / API 26 o superior).
5. Presionar **Run** (`Shift + F10`).