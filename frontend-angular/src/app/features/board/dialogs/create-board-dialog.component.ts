import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { MatDialogRef } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BoardService } from '../../../core/services/board.service';

@Component({
    selector: 'app-create-board-dialog',
    template: `
        <h2 mat-dialog-title>Create New Board</h2>
        <mat-dialog-content>
            <form [formGroup]="boardForm">
                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Board Name</mat-label>
                    <input matInput formControlName="name" required>
                    <mat-error *ngIf="boardForm.get('name')?.hasError('required')">
                        Board name is required
                    </mat-error>
                </mat-form-field>

                <mat-form-field appearance="outline" class="full-width">
                    <mat-label>Description</mat-label>
                    <textarea matInput formControlName="description" rows="3"></textarea>
                </mat-form-field>
            </form>
        </mat-dialog-content>

        <mat-dialog-actions align="end">
            <button mat-button (click)="onCancel()">Cancel</button>
            <button 
                mat-raised-button 
                color="primary" 
                (click)="onSubmit()"
                [disabled]="boardForm.invalid || isLoading"
            >
                <mat-spinner diameter="20" *ngIf="isLoading"></mat-spinner>
                <span *ngIf="!isLoading">Create Board</span>
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
export class CreateBoardDialogComponent {
    boardForm: FormGroup;
    isLoading = false;

    constructor(
        private fb: FormBuilder,
        private boardService: BoardService,
        private dialogRef: MatDialogRef<CreateBoardDialogComponent>,
        private snackBar: MatSnackBar
    ) {
        this.boardForm = this.fb.group({
            name: ['', Validators.required],
            description: ['']
        });
    }

    onSubmit(): void {
        if (this.boardForm.valid) {
            this.isLoading = true;
            this.boardService.create(this.boardForm.value).subscribe({
                next: (board) => {
                    this.dialogRef.close(board);
                },
                error: (error) => {
                    this.isLoading = false;
                    this.snackBar.open(error.message || 'Failed to create board', 'Close', {
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