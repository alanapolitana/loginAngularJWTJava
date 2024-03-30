import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services/auth/login.service';
import { LoginRequest } from 'src/app/services/auth/loginRequest';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  loginError: string = "";
  loginForm = this.formBuilder.group({
    username: ['marcovirinni@gmail.com', [Validators.required, Validators.email]],
    password: ['123456', Validators.required],
  });

  constructor(private formBuilder: FormBuilder, private router: Router, private loginService: LoginService) { }

  ngOnInit(): void {}

  get email() {
    return this.loginForm.controls.username;
  }

  get password() {
    return this.loginForm.controls.password;
  }

  login() {
    if (this.loginForm.valid) {
      this.loginError = "";
      this.loginService.login(this.loginForm.value as LoginRequest).subscribe({
        next: (userData) => {
          console.log(userData);
          this.redirectAfterLogin(userData.role); // Redirige según el rol del usuario
        },
        error: (errorData) => {
          console.error(errorData);
          this.loginError = errorData;
        },
        complete: () => {
          console.info("Inicio de sesión completado");
          this.loginForm.reset();
        }
      });
    } else {
      this.loginForm.markAllAsTouched();
      alert("Error al ingresar los datos.");
    }
  }

  // Función para redirigir según el rol del usuario
  private redirectAfterLogin(role: string) {
    switch (role) {
      case 'ADMIN':
        this.router.navigateByUrl('/admin-dashboard');
        break;
      case 'USER':
        this.router.navigateByUrl('/dashboard');
        break;
      default:
        // Manejar otros roles aquí o redirigir a una página por defecto
        this.router.navigateByUrl('/inicio');
        break;
    }
  }
}
