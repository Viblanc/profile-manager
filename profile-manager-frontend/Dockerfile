# Stage 1: Build the Angular application
FROM node:22-alpine AS build
WORKDIR /app
COPY package.json package-lock.json nginx.conf ./
RUN npm -g install @angular/cli
RUN npm ci
COPY . .
RUN ng build --configuration=production

# Stage 2: Serve the application with Nginx
FROM nginx:stable-alpine
COPY --from=build /app/dist/profile-manager-frontend/browser /usr/share/nginx/html
COPY --from=build /app/nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]