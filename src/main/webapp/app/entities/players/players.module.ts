import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PlayersComponent } from './list/players.component';
import { PlayersDetailComponent } from './detail/players-detail.component';
import { PlayersUpdateComponent } from './update/players-update.component';
import { PlayersDeleteDialogComponent } from './delete/players-delete-dialog.component';
import { PlayersRoutingModule } from './route/players-routing.module';

@NgModule({
  imports: [SharedModule, PlayersRoutingModule],
  declarations: [PlayersComponent, PlayersDetailComponent, PlayersUpdateComponent, PlayersDeleteDialogComponent],
  entryComponents: [PlayersDeleteDialogComponent],
})
export class PlayersModule {}
