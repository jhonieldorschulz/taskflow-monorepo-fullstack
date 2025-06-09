import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material/snack-bar';

@Injectable()
export class ErrorInterceptor implements HttpInterceptor {
    constructor(
        private router: Router,
        private snackBar: MatSnackBar
    ) {}

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(request).pipe(
            catchError((error: HttpErrorResponse) => {
                let errorMessage = 'An error occurred';

                if (error.error instanceof ErrorEvent) {
                    // Client-side error
                    errorMessage = error.error.message;
                } else {
                    // Server-side error
                    if (error.status === 401) {
                        localStorage.removeItem('token');
                        localStorage.removeItem('user');
                        this.router.navigate(['/auth/login']);
                        errorMessage = 'Session expired. Please login again.';
                    } else if (error.error?.message) {
                        errorMessage = error.error.message;
                    } else {
                        errorMessage = `Error Code: ${error.status}\nMessage: ${error.message}`;
                    }
                }

                this.snackBar.open(errorMessage, 'Close', {
                    duration: 5000,
                    horizontalPosition: 'center',
                    verticalPosition: 'bottom'
                });

                return throwError(() => error);
            })
        );
    }
} 