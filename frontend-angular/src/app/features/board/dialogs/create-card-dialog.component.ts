import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CardService } from '../../../core/services/card.service';

interface DialogData {
    listId: number;
}

@Component({
    selector: 'app-create-card-dialog',
    template: `
        <h2 mat-dialog-title>Create New Card</h2>
        <mat-dialog-content>
            <form [formGroup]="cardForm">
                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Title</mat-label>
                    <input matInput formControlName="title" required>
                    <mat-error *ngIf="cardForm.get('title')?.hasError('required')">
                        Title is required
                    </mat-error>
                </mat-form-field>

                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Description</mat-label>
                    <textarea matInput formControlName="description" rows="3"></textarea>
                </mat-form-field>

                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Due Date</mat-label>
                    <input matInput [matDatepicker]="picker" formControlName="dueDate">
                    <mat-datepicker-toggle matSuffix [for]="picker"></mat-datepicker-toggle>
                    <mat-datepicker #picker></mat-datepicker>
                </mat-form-field>

                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Priority</mat-label>
                    <mat-select formControlName="priority">
                        <mat-option value="HIGH">High</mat-option>
                        <mat-option value="MEDIUM">Medium</mat-option>
                        <mat-option value="LOW">Low</mat-option>
                    </mat-select>
                </mat-form-field>
            </form>
        </mat-dialog-content>

        <mat-dialog-actions align="end">
            <button mat-button (click)="onCancel()">Cancel</button>
            <button 
                mat-raised-button 
                color="primary" 
                (click)="onSubmit()"
                [disabled]="cardForm.invalid || isLoading"
            >
                <mat-spinner diameter="20" *ngIf="isLoading"></mat-spinner>
                <span *ngIf="!isLoading">Create Card</span>
            </button>
        </mat-dialog-actions>
    `,
    styles: [`
        .full-width {
            width: 100%;
        }

        mat-dialog-content {
            min-width: 300px;
        }

        form {
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        mat-dialog-actions {
            padding: 16px 0 0;
        }

        button[mat-raised-button] {
            min-width: 120px;
            height: 36px;
        }

        mat-spinner {
            margin: 0 auto;
        }
    `]
})
export class CreateCardDialogComponent {
    cardForm: FormGroup;
    isLoading = false;

    constructor(
        private fb: FormBuilder,
        private cardService: CardService,
        private dialogRef: MatDialogRef<CreateCardDialogComponent>,
        @Inject(MAT_DIALOG_DATA) private data: DialogData,
        private snackBar: MatSnackBar
    ) {
        this.cardForm = this.fb.group({
            title: ['', Validators.required],
            description: [''],
            dueDate: [null],
            priority: ['MEDIUM']
        });
    }

    onSubmit(): void {
        if (this.cardForm.valid) {
            this.isLoading = true;
            const request = {
                ...this.cardForm.value,
                listId: this.data.listId
            };

            this.cardService.create(request).subscribe({
                next: (card) => {
                    this.dialogRef.close(card);
                },
                error: (error) => {
                    this.isLoading = false;
                    this.snackBar.open(error.message || 'Failed to create card', 'Close', {
                        duration: 5000
                    });
                }
            });
        }
    }

    onCancel(): void {
        this.dialogRef.close();
    }
} 