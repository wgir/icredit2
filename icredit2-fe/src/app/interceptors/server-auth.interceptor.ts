import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { REQUEST } from '../tokens';

export const serverAuthInterceptor: HttpInterceptorFn = (req, next) => {
    const serverReq = inject(REQUEST, { optional: true });
    let authReq = req;

    // Handle full URL for requests on the server
    if (req.url.startsWith('/')) {
        const baseUrl = 'http://localhost:8080'; // Target backend directly
        const fullUrl = `${baseUrl}${req.url}`;
        authReq = req.clone({
            url: fullUrl
        });
    }

    if (serverReq) {
        const cookies = serverReq.headers.cookie;
        if (cookies) {
            authReq = authReq.clone({
                headers: authReq.headers.set('Cookie', cookies)
            });
        }
    }

    return next(authReq);
};
