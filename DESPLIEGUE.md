# Despliegue — Ecopetrol 2.0 (entorno *2*)

La aplicación modernizada despliega sobre la infraestructura cuyo nombre termina en **`2`**
(separada del entorno original). El despliegue es **automático al hacer merge a `main`**.

## Mapa de infraestructura (entorno 2)

| Componente | Recurso Azure |
|---|---|
| Frontend (Angular) | App Service **`app-central-ecopetrol2`** |
| Backend (Spring Boot) | ACI **`aci-centralecopetrol2`** (RG `rg-central-solucion-talento2`) |
| Imagen del backend | ACR **`acrecopetrolntt2.azurecr.io/talento-back`** |
| Base de datos | Azure SQL **`sqlserver-ecopetrol2` / `ecopetroldb2`** |

## Pipelines (CI/CD)

Al hacer push/merge a `main`:

- **`back/**` → `backend-deploy.yml`**: compila el JAR → construye y sube la imagen a **ACR2** → reinicia **ACI2** (`az container restart`) para tomar la imagen nueva.
- **`front/**` → `frontend-deploy.yml`**: build de Angular → empaqueta `server.js` + `dist/mocks-ecopetrol` → despliega a **App Service2**.

> El paso de reinicio del ACI se **salta** (run en verde) si falta el secret `AZURE_CREDENTIALS`, hasta que se configure.

## Secrets de GitHub requeridos

| Secret | Para qué |
|---|---|
| `ACR2_USERNAME` / `ACR2_PASSWORD` | Push de la imagen a `acrecopetrolntt2` (`az acr credential show -n acrecopetrolntt2`) |
| `AZURE_CREDENTIALS` | Service Principal (JSON `azure/login`) con rol Contributor sobre `rg-central-solucion-talento2` (o sobre el ACI2) para reiniciarlo |
| `AZURE_WEBAPP_PUBLISH_PROFILE2` | Publish profile de `app-central-ecopetrol2` |

## Pendientes de infraestructura (una vez)

1. **ACI2** hoy corre `aci-helloworld`. Hay que (re)crearlo con la imagen `acrecopetrolntt2.azurecr.io/talento-back:latest`, inyectado en la VNet del entorno 2 y con variables de entorno:
   `ECOPETROL_DB_URL` (→ `ecopetroldb2`), `ECOPETROL_DB_USERNAME`, `ECOPETROL_DB_PASSWORD`.
2. **App Service2** está *Stopped*: arrancarlo y configurar `BACKEND_URL` apuntando a la IP privada del ACI2 (el `server.js` proxya `/autenticacion`, `/vacaciones`, `/incapacidad`, `/cumpleanio`, `/calamidad`, `/administracion`, `/usuario`).
3. **Secretos en código** (JWT `JwtUtil`, clave AES `AesUtil`): moverlos a variables de entorno / Key Vault antes de producción.

## Correr en local (sin tocar Azure)

**Backend (H2 en memoria, datos semilla):**
```bash
cd back/talento
./gradlew bootRun --args='--spring.profiles.active=h2'   # http://localhost:8080
```
**Frontend (proxy a localhost:8080):**
```bash
cd front
npm install
npm start            # http://localhost:4200
```
**Usuarios semilla** (password `Cl4v3T3mp0r4l2026`): `cmedina` (EMPLEADO), `nvivas` (LIDER), `jtorres` (PEOPLE).
