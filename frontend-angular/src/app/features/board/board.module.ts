import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { DragDropModule } from '@angular/cdk/drag-drop';
import { MatModule } from '../../shared/mat.module';

import { BoardListComponent } from './board-list/board-list.component';
import { BoardDetailComponent } from './board-detail/board-detail.component';
import { CreateBoardDialogComponent } from './dialogs/create-board-dialog.component';
import { CreateListDialogComponent } from './dialogs/create-list-dialog.component';
import { CreateCardDialogComponent } from './dialogs/create-card-dialog.component';
import { BoardRoutingModule } from './board-routing.module';

@NgModule({
    declarations: [
        BoardListComponent,
        BoardDetailComponent,
        CreateBoardDialogComponent,
        CreateListDialogComponent,
        CreateCardDialogComponent
    ],
    imports: [
        CommonModule,
        ReactiveFormsModule,
        RouterModule,
        DragDropModule,
        MatModule,
        BoardRoutingModule
    ]
})
export class BoardModule { } 