import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import { CdkDragDrop, moveItemInArray, transferArrayItem } from '@angular/cdk/drag-drop';
import { BoardService } from '../../../core/services/board.service';
import { TaskListService } from '../../../core/services/task-list.service';
import { CardService } from '../../../core/services/card.service';
import { Board } from '../../../core/models/board.model';
import { TaskList } from '../../../core/models/task-list.model';
import { Card } from '../../../core/models/card.model';
import { CreateListDialogComponent } from '../dialogs/create-list-dialog.component';
import { CreateCardDialogComponent } from '../dialogs/create-card-dialog.component';

@Component({
    selector: 'app-board-detail',
    template: `
        <div class="container" *ngIf="board">
            <div class="header">
                <div class="title">
                    <h1>{{ board.name }}</h1>
                    <p *ngIf="board.description">{{ board.description }}</p>
                </div>
                <div class="actions">
                    <button mat-raised-button color="primary" (click)="openCreateListDialog()">
                        <mat-icon>add</mat-icon>
                        Add List
                    </button>
                </div>
            </div>

            <div class="board-content" cdkDropListGroup>
                <div class="list-container" *ngFor="let list of lists">
                    <mat-card class="list">
                        <mat-card-header>
                            <mat-card-title>{{ list.name }}</mat-card-title>
                            <button mat-icon-button [matMenuTriggerFor]="listMenu">
                                <mat-icon>more_vert</mat-icon>
                            </button>
                            <mat-menu #listMenu="matMenu">
                                <button mat-menu-item (click)="deleteList(list)">
                                    <mat-icon>delete</mat-icon>
                                    <span>Delete List</span>
                                </button>
                            </mat-menu>
                        </mat-card-header>

                        <mat-card-content>
                            <div
                                cdkDropList
                                [cdkDropListData]="list.cards"
                                (cdkDropListDropped)="dropCard($event)"
                                class="card-list"
                            >
                                <mat-card 
                                    *ngFor="let card of list.cards" 
                                    class="card"
                                    cdkDrag
                                >
                                    <mat-card-header>
                                        <mat-card-title>{{ card.title }}</mat-card-title>
                                        <button mat-icon-button [matMenuTriggerFor]="cardMenu">
                                            <mat-icon>more_vert</mat-icon>
                                        </button>
                                        <mat-menu #cardMenu="matMenu">
                                            <button mat-menu-item (click)="deleteCard(card)">
                                                <mat-icon>delete</mat-icon>
                                                <span>Delete Card</span>
                                            </button>
                                        </mat-menu>
                                    </mat-card-header>
                                    <mat-card-content *ngIf="card.description">
                                        <p>{{ card.description }}</p>
                                    </mat-card-content>
                                    <mat-card-footer *ngIf="card.dueDate || card.assignee">
                                        <div class="card-meta">
                                            <span *ngIf="card.dueDate" class="due-date">
                                                <mat-icon>event</mat-icon>
                                                {{ card.dueDate | date }}
                                            </span>
                                            <span *ngIf="card.assignee" class="assignee">
                                                <mat-icon>person</mat-icon>
                                                {{ card.assignee.name }}
                                            </span>
                                        </div>
                                    </mat-card-footer>
                                </mat-card>
                            </div>
                        </mat-card-content>

                        <mat-card-actions>
                            <button mat-button (click)="openCreateCardDialog(list)">
                                <mat-icon>add</mat-icon>
                                Add Card
                            </button>
                        </mat-card-actions>
                    </mat-card>
                </div>
            </div>
        </div>
    `,
    styles: [`
        .container {
            padding: 24px;
            height: 100%;
        }

        .header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            margin-bottom: 24px;
        }

        .title {
            h1 {
                margin: 0 0 8px;
            }

            p {
                margin: 0;
                color: rgba(0, 0, 0, 0.6);
            }
        }

        .board-content {
            display: flex;
            gap: 24px;
            overflow-x: auto;
            padding-bottom: 24px;
        }

        .list-container {
            width: 280px;
            flex-shrink: 0;
        }

        .list {
            background: #f5f5f5;

            mat-card-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 16px;

                mat-card-title {
                    margin: 0;
                    font-size: 16px;
                }
            }

            mat-card-content {
                padding: 8px;
            }

            mat-card-actions {
                padding: 8px;
                margin: 0;
            }
        }

        .card-list {
            min-height: 60px;
        }

        .card {
            margin-bottom: 8px;
            cursor: move;

            &:last-child {
                margin-bottom: 0;
            }

            mat-card-header {
                padding: 12px 12px 0;

                mat-card-title {
                    font-size: 14px;
                    margin: 0;
                }
            }

            mat-card-content {
                padding: 12px;
                font-size: 14px;

                p {
                    margin: 0;
                }
            }
        }

        .card-meta {
            display: flex;
            gap: 16px;
            padding: 8px 12px;
            font-size: 12px;
            color: rgba(0, 0, 0, 0.6);

            span {
                display: flex;
                align-items: center;
                gap: 4px;

                mat-icon {
                    font-size: 16px;
                    width: 16px;
                    height: 16px;
                }
            }
        }

        .cdk-drag-preview {
            box-sizing: border-box;
            border-radius: 4px;
            box-shadow: 0 5px 5px -3px rgba(0, 0, 0, 0.2),
                        0 8px 10px 1px rgba(0, 0, 0, 0.14),
                        0 3px 14px 2px rgba(0, 0, 0, 0.12);
        }

        .cdk-drag-placeholder {
            opacity: 0;
        }

        .cdk-drag-animating {
            transition: transform 250ms cubic-bezier(0, 0, 0.2, 1);
        }

        .card-list.cdk-drop-list-dragging .card:not(.cdk-drag-placeholder) {
            transition: transform 250ms cubic-bezier(0, 0, 0.2, 1);
        }
    `]
})
export class BoardDetailComponent implements OnInit {
    board: Board | null = null;
    lists: TaskList[] = [];

    constructor(
        private route: ActivatedRoute,
        private boardService: BoardService,
        private taskListService: TaskListService,
        private cardService: CardService,
        private dialog: MatDialog,
        private snackBar: MatSnackBar
    ) {}

    ngOnInit(): void {
        const boardId = this.route.snapshot.params['id'];
        this.loadBoard(boardId);
    }

    loadBoard(boardId: number): void {
        this.boardService.getById(boardId).subscribe({
            next: (board) => {
                this.board = board;
                this.loadLists(boardId);
            },
            error: (error) => {
                this.snackBar.open(error.message || 'Failed to load board', 'Close', {
                    duration: 5000
                });
            }
        });
    }

    loadLists(boardId: number): void {
        this.taskListService.getAllByBoard(boardId).subscribe({
            next: (lists) => {
                this.lists = lists;
            },
            error: (error) => {
                this.snackBar.open(error.message || 'Failed to load lists', 'Close', {
                    duration: 5000
                });
            }
        });
    }

    openCreateListDialog(): void {
        if (!this.board) return;

        const dialogRef = this.dialog.open(CreateListDialogComponent, {
            width: '400px',
            data: { boardId: this.board.id }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.loadLists(this.board!.id);
            }
        });
    }

    openCreateCardDialog(list: TaskList): void {
        const dialogRef = this.dialog.open(CreateCardDialogComponent, {
            width: '400px',
            data: { listId: list.id }
        });

        dialogRef.afterClosed().subscribe(result => {
            if (result) {
                this.loadLists(this.board!.id);
            }
        });
    }

    deleteList(list: TaskList): void {
        if (confirm('Are you sure you want to delete this list?')) {
            this.taskListService.delete(list.id).subscribe({
                next: () => {
                    this.loadLists(this.board!.id);
                },
                error: (error) => {
                    this.snackBar.open(error.message || 'Failed to delete list', 'Close', {
                        duration: 5000
                    });
                }
            });
        }
    }

    deleteCard(card: Card): void {
        if (confirm('Are you sure you want to delete this card?')) {
            this.cardService.delete(card.id).subscribe({
                next: () => {
                    this.loadLists(this.board!.id);
                },
                error: (error) => {
                    this.snackBar.open(error.message || 'Failed to delete card', 'Close', {
                        duration: 5000
                    });
                }
            });
        }
    }

    dropCard(event: CdkDragDrop<Card[]>): void {
        if (event.previousContainer === event.container) {
            moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
            this.updateCardPositions(event.container.data);
        } else {
            transferArrayItem(
                event.previousContainer.data,
                event.container.data,
                event.previousIndex,
                event.currentIndex
            );
            
            const card = event.container.data[event.currentIndex];
            const newListId = this.lists.find(list => list.cards === event.container.data)?.id;
            
            if (newListId) {
                this.cardService.moveCard(card.id, {
                    listId: newListId,
                    position: event.currentIndex
                }).subscribe({
                    error: (error) => {
                        this.snackBar.open(error.message || 'Failed to move card', 'Close', {
                            duration: 5000
                        });
                        this.loadLists(this.board!.id);
                    }
                });
            }
        }
    }

    private updateCardPositions(cards: Card[]): void {
        cards.forEach((card, index) => {
            this.cardService.update(card.id, { position: index }).subscribe({
                error: (error) => {
                    this.snackBar.open(error.message || 'Failed to update card position', 'Close', {
                        duration: 5000
                    });
                    this.loadLists(this.board!.id);
                }
            });
        });
    }
} 