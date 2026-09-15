import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {

  protected readonly errorLogin = signal(false);
  protected readonly enviando = signal(false);

  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  protected readonly formulario = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });

  protected onSubmit(): void {
    if (this.formulario.invalid) {
      this.formulario.markAllAsTouched();
      return;
    }

    const { username, password } = this.formulario.getRawValue();
    this.errorLogin.set(false);
    this.enviando.set(true);
    this.authService.login(username ?? '', password ?? '').subscribe({
      next: () => {
        this.enviando.set(false);
        this.router.navigateByUrl('/dashboard');
      },
      error: () => {
        this.enviando.set(false);
        this.errorLogin.set(true);
      },
    });
  }

}
