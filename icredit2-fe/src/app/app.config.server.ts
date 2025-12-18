import { mergeApplicationConfig, ApplicationConfig } from '@angular/core';
import { provideServerRendering, withRoutes } from '@angular/ssr';
import { appConfig } from './app.config';
import { serverRoutes } from './app.routes.server';
import { provideHttpClient, withFetch, withInterceptors } from '@angular/common/http';
import { serverAuthInterceptor } from './interceptors/server-auth.interceptor';

const serverConfig: ApplicationConfig = {
  providers: [
    provideServerRendering(withRoutes(serverRoutes)),
    provideHttpClient(withFetch(), withInterceptors([serverAuthInterceptor]))
  ]
};

export const config = mergeApplicationConfig(appConfig, serverConfig);
