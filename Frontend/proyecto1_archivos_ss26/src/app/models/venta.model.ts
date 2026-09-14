export interface Venta {
}

export interface DetalleVentaRequest {
    idProducto: number;
    cantidad: number;
}

export interface VentaRequest {
    idCliente: number;
    username: string;
    items: DetalleVentaRequest[];
}

export interface DetalleVentaResponse {
    idDetalleVenta: number;
    idProducto: number;
    nombreProducto: string;
    cantidad: number;
    precioUnitarioVenta: number;
    subtotal: number;
    costoUnitarioPromedio: number;
    costoTotal: number;
}

export interface VentaResponse {
    idVenta: number;
    idCliente: number;
    nombreCliente: string;
    username: string;
    fechaVenta: string;
    subtotal: number;
    iva: number;
    total: number;
    detalles: DetalleVentaResponse[];
}
