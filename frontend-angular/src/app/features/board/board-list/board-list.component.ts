import { Component, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { BoardService } from '../../../core/services/board.service';
import { Board } from '../../../core/models/board.model';
import { CreateBoardDialogComponent } from '../dialogs/create-board-dialog.component';

@Component({
    selector: 'app-board-list',
    template: `
        <div class="container">
            <div class="header">
                <h1>My Boards</h1>
                <button mat-raised-button color="primary" (click)="openCreateDialog()">
                    <mat-icon>add</mat-icon>
                    Create Board
                </button>
            </div>

            <div class="boards-grid" *ngIf="boards.length > 0">
                <mat-card *ngFor="let board of boards" [routerLink]="['/boards', board.id]" class="board-card">
                    <mat-card-header>
                        <mat-card-title>{{ board.name }}</mat-card-title>
                        <mat-card-subtitle *ngIf="board.description">{{ board.description }}</mat-card-subtitle>
                    </mat-card-header>
                    <mat-card-content>
                        <p class="members-count" *ngIf="board.members?.length">
                            <mat-icon>group</mat-icon>
                            {{ board.members.length }} members
                        </p>
                    </mat-card-content>
                </mat-card>
            </div>

            <div class="empty-state" *ngIf="boards.length === 0">
                <mat-icon>dashboard</mat-icon>
                <h2>No boards yet</h2>
                <p>Create your first board to get started</p>
                <button mat-raised-button color="primary" (click)="openCreateDialog()">
                    Create Board
                </button>
            </div>
        </div>
    `,
    styles: [`
        .container {
            padding: 24px;
            max-width: 1200px;
            margin: 0 auto;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 24px;
        }

        .boards-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
            gap: 24px;
        }

        .board-card {
            cursor: pointer;
            transition: transform 0.2s;

            &:hover {
                transform: translateY(-4px);
            }
        }

        .members-count {
            display: flex;
            align-items: center;
            gap: 8px;
            color: rgba(0, 0, 0, 0.6);
            margin: 0;

            mat-icon {
                font-size: 18px;
                width: 18px;
                height: 18px;
            }
        }

        .empty-state {
            text-align: center;
            padding: 48px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

            mat-icon {
                font-size: 48px;
                width: 48px;
                height: 48px;
                margin-bottom: 16px;
                color: rgba(0, 0, 0, 0.4);
            }

            h2 {
                margin: 0 0 8px;
                color: rgba(0, 0, 0, 0.87);
            }

            p {
                margin: 0 0 24px;
                color: rgba(0, 0, 0, 0.6);
            }
        }
    `]
})
export class BoardListComponent implements OnInit {
    boards: Board[] = [];

    constructor(
        private boardService: BoardService,
        private dialog: MatDialog,
        private snackBar: MatSnackBar
    ) {}

    ngOnInit(): void {
        this.loadBoards();
    }

    loadBoards(): void {
        this.boardService.getAll().subscribe({
            next: (boards) => {
                this.boards = boards;
            },
            error: (error) => {
                this.snackBar.open(error.message || 'Failed to load boards', 'Close', {
                    duration: 5000
                });
            }
        });
    }

    openCreateDialog(): void {
        const dialogRef = this.dialog.open(CreateBoardDialogComponent, {
            width: '400px'
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.loadBoards();
            }
        });
    }
} 