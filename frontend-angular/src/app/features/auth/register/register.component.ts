import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';
import { AuthService } from '../../../core/services/auth.service';

@Component({
    selector: 'app-register',
    template: `
        <div class="container">
            <mat-card class="register-card">
                <mat-card-header>
                    <mat-card-title>Register for TaskFlow</mat-card-title>
                </mat-card-header>

                <mat-card-content>
                    <form [formGroup]="registerForm" (ngSubmit)="onSubmit()">
                        <mat-form-field appearance="outline">
                            <mat-label>Name</mat-label>
                            <input matInput formControlName="name" required>
                            <mat-error *ngIf="registerForm.get('name')?.hasError('required')">
                                Name is required
                            </mat-error>
                        </mat-form-field>

                        <mat-form-field appearance="outline">
                            <mat-label>Email</mat-label>
                            <input matInput type="email" formControlName="email" required>
                            <mat-error *ngIf="registerForm.get('email')?.hasError('required')">
                                Email is required
                            </mat-error>
                            <mat-error *ngIf="registerForm.get('email')?.hasError('email')">
                                Please enter a valid email
                            </mat-error>
                        </mat-form-field>

                        <mat-form-field appearance="outline">
                            <mat-label>Password</mat-label>
                            <input matInput type="password" formControlName="password" required>
                            <mat-error *ngIf="registerForm.get('password')?.hasError('required')">
                                Password is required
                            </mat-error>
                            <mat-error *ngIf="registerForm.get('password')?.hasError('minlength')">
                                Password must be at least 6 characters
                            </mat-error>
                        </mat-form-field>

                        <button mat-raised-button color="primary" type="submit" [disabled]="registerForm.invalid || isLoading">
                            <mat-spinner diameter="20" *ngIf="isLoading"></mat-spinner>
                            <span *ngIf="!isLoading">Register</span>
                        </button>
                    </form>
                </mat-card-content>

                <mat-card-actions>
                    <a mat-button routerLink="/auth/login">Already have an account? Login</a>
                </mat-card-actions>
            </mat-card>
        </div>
    `,
    styles: [`
        .container {
            height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            background: #f5f5f5;
        }

        .register-card {
            width: 100%;
            max-width: 400px;
            margin: 20px;
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 16px;
            margin-top: 20px;
        }

        mat-card-actions {
            display: flex;
            justify-content: center;
            padding: 16px;
        }

        button[type="submit"] {
            height: 36px;
        }

        mat-spinner {
            margin: 0 auto;
        }
    `]
})
export class RegisterComponent {
    registerForm: FormGroup;
    isLoading = false;

    constructor(
        private fb: FormBuilder,
        private authService: AuthService,
        private router: Router,
        private snackBar: MatSnackBar
    ) {
        this.registerForm = this.fb.group({
            name: ['', Validators.required],
            email: ['', [Validators.required, Validators.email]],
            password: ['', [Validators.required, Validators.minLength(6)]]
        });
    }

    onSubmit(): void {
        if (this.registerForm.valid) {
            this.isLoading = true;
            this.authService.register(this.registerForm.value).subscribe({
                next: () => {
                    this.router.navigate(['/boards']);
                },
                error: (error) => {
                    this.isLoading = false;
                    this.snackBar.open(error.message || 'Registration failed', 'Close', {
                        duration: 5000
                    });
                }
            });
        }
    }
} 