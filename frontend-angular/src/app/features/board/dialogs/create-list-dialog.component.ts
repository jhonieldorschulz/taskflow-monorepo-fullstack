import { Component, Inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { TaskListService } from '../../../core/services/task-list.service';

interface DialogData {
    boardId: number;
}

@Component({
    selector: 'app-create-list-dialog',
    template: `
        <h2 mat-dialog-title>Create New List</h2>
        <mat-dialog-content>
            <form [formGroup]="listForm">
                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>List Name</mat-label>
                    <input matInput formControlName="name" required>
                    <mat-error *ngIf="listForm.get('name')?.hasError('required')">
                        List name is required
                    </mat-error>
                </mat-form-field>
            </form>
        </mat-dialog-content>

        <mat-dialog-actions align="end">
            <button mat-button (click)="onCancel()">Cancel</button>
            <button 
                mat-raised-button 
                color="primary" 
                (click)="onSubmit()"
                [disabled]="listForm.invalid || isLoading"
            >
                <mat-spinner diameter="20" *ngIf="isLoading"></mat-spinner>
                <span *ngIf="!isLoading">Create List</span>
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
export class CreateListDialogComponent {
    listForm: FormGroup;
    isLoading = false;

    constructor(
        private fb: FormBuilder,
        private taskListService: TaskListService,
        private dialogRef: MatDialogRef<CreateListDialogComponent>,
        @Inject(MAT_DIALOG_DATA) private data: DialogData,
        private snackBar: MatSnackBar
    ) {
        this.listForm = this.fb.group({
            name: ['', Validators.required]
        });
    }

    onSubmit(): void {
        if (this.listForm.valid) {
            this.isLoading = true;
            const request = {
                ...this.listForm.value,
                boardId: this.data.boardId
            };

            this.taskListService.create(request).subscribe({
                next: (list) => {
                    this.dialogRef.close(list);
                },
                error: (error) => {
                    this.isLoading = false;
                    this.snackBar.open(error.message || 'Failed to create list', 'Close', {
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