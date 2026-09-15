import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { catchError, throwError } from 'rxjs';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  const token = authService.getToken();
  const peticionConToken = token
    ? req.clone({ setHeaders: { Authorization: `Bearer ${token}` } })
    : req;
  return next(peticionConToken).pipe(
    catchError((error: HttpErrorResponse) => {
      // 401 = token no existe/es invalido/expiro = Unauthorized
      if (error.status === 401) {
        authService.logout();
        router.navigateByUrl('/login');
      }
      return throwError(() => error);
    }),
  );
};
