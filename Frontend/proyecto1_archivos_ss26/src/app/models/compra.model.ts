export interface Compra {
}

export interface DetalleCompraRequest {
    idProducto: number;
    cantidad: number;
    precioUnitario: number;
}

export interface CompraRequest {
    idProveedor: number;
    username: string;
    items: DetalleCompraRequest[];
}

export interface DetalleCompraResponse {
    idDetalleCompra: number;
    idProducto: number;
    nombreProducto: string;
    cantidad: number;
    precioUnitario: number;
    subtotal: number;
}

export interface CompraResponse {
    idCompra: number;
    idProveedor: number;
    nombreProveedor: string;
    username: string;
    fechaCompra: string;
    total: number;
    detalles: DetalleCompraResponse[];
}
