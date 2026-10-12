# Deploying on Render with PostgreSQL

1. Create a PostgreSQL database on Render.
2. Create a Web Service from the repository and select **Docker**. Set the service's root directory to `backend/employee-management`, so Render uses this directory's `Dockerfile`.
3. Add these environment variables to the Web Service, using the database's **internal** connection details:

   | Variable | Value |
   | --- | --- |
   | `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<internal-host>:5432/<database-name>` |
   | `SPRING_DATASOURCE_USERNAME` | The database user |
   | `SPRING_DATASOURCE_PASSWORD` | The database password |
   | `APP_JWT_SECRET` | A Base64-encoded random key of at least 32 bytes; generate one with `openssl rand -base64 32` |

   `APP_JWT_EXPIRATION_MS` is optional and defaults to `3600000` (one hour). Render sets `PORT` automatically; the application listens on it.

4. In the Vercel project for the frontend, set `VITE_API_BASE_URL` to `https://peopleflow-backend.onrender.com/api` and redeploy the frontend. The frontend also uses this as its production default. The backend allows the frontend origin `https://employee-management-system-frontend-psi-five.vercel.app` and the local Vite origin.

The service uses Hibernate's `update` mode to create or update tables on startup. This does not migrate existing MySQL data; migrate that data separately if needed. Keep the datasource credentials and JWT secret in Render's environment settings, not in source control.
