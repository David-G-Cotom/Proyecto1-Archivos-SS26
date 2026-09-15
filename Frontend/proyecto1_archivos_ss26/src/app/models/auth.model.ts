import { UsuarioSesion } from "./usuario-sesion.model";

export interface Auth {
}

export interface LoginRequest {
    username: string;
    password: string;
}

export interface LoginResponse {
    token: string;
    username: string;
    nombreCompleto: string;
    rol: string;
}

export interface SesionGuardada {
    token: string;
    usuario: UsuarioSesion;
}
