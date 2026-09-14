export interface Producto {
    idProducto: number;
    idCategoria: number;
    nombreCategoria: string;
    nombre: string;
    descripcion: string | null;
    precioVenta: number;
    stockMinimo: number;
    activo: boolean;
}

export interface ProductoRequest {
    idCategoria: number;
    nombre: string;
    descripcion: string | null;
    precioVenta: number;
    stockMinimo: number;
}
