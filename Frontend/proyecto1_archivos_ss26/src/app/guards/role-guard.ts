import { CanActivateChildFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { inject } from '@angular/core';
import { RoleService } from '../services/role.service';

export const roleGuard: CanActivateChildFn = (childRoute, state) => {
  const auth = inject(AuthService);
  const role = inject(RoleService);
  const router = inject(Router);

  const usuario = auth.usuarioActual();
  if (!usuario) {
    return router.createUrlTree(['/login']);
  }

  const rutaSolicitada = childRoute.routeConfig?.path ?? '';
  if (role.rolPuedeAcceder(usuario.rol, rutaSolicitada)) {
    return true;
  }

  return router.createUrlTree(['/dashboard']);
};
