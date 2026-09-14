export interface Cliente {
    idCliente: number;
    nombre: string;
    nit: string | null;
    telefono: string | null;
    email: string | null;
    direccion: string | null;
    activo: boolean;
}

export interface ClienteRequest {
    nombre: string;
    nit: string | null;
    telefono: string | null;
    email: string | null;
    direccion: string | null;
}
