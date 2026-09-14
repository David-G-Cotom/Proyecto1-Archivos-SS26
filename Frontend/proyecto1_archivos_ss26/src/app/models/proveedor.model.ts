export interface Proveedor {
    idProveedor: number;
    nombre: string;
    nit: string | null;
    telefono: string | null;
    email: string | null;
    direccion: string | null;
    activo: boolean;
}

export interface ProveedorRequest {
    nombre: string;
    nit: string | null;
    telefono: string | null;
    email: string | null;
    direccion: string | null;
}
