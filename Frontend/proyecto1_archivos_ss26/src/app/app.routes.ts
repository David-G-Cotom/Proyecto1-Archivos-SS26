import { Routes } from '@angular/router';
import { MainLayoutComponent } from './layout/main-layout/main-layout.component';
import { authGuard } from './guards/auth-guard';
import { roleGuard } from './guards/role-guard';

export const routes: Routes = [
    {
        path: 'login',
        loadComponent: () =>
            import('./pages/login/login.component').then((c) => c.LoginComponent),
    },
    {
        path: '',
        component: MainLayoutComponent,
        canActivate: [authGuard],
        canActivateChild: [roleGuard],
        children: [
            {
                path: '',
                redirectTo: 'dashboard',
                pathMatch: 'full'
            },
            {
                path: 'dashboard',
                loadComponent: () =>
                    import('./pages/dashboard/dashboard.component').then((c) => c.DashboardComponent),
            },
            {
                path: 'productos',
                loadComponent: () =>
                    import('./pages/productos/productos.component').then((c) => c.ProductosComponent),
            },
            {
                path: 'proveedores',
                loadComponent: () =>
                    import('./pages/proveedores/proveedores.component').then((c) => c.ProveedoresComponent),
            },
            {
                path: 'clientes',
                loadComponent: () =>
                    import('./pages/clientes/clientes.component').then((c) => c.ClientesComponent),
            },
            {
                path: 'compras',
                loadComponent: () =>
                    import('./pages/compras/compras.component').then((c) => c.ComprasComponent),
            },
            {
                path: 'ventas',
                loadComponent: () =>
                    import('./pages/ventas/ventas.component').then((c) => c.VentasComponent),
            },
        ]
    },
    {
        path: '**',
        redirectTo: ''
    }
];
