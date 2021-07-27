import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { PlayersAuditComponent } from './list/players-audit.component';
import { PlayersAuditDetailComponent } from './detail/players-audit-detail.component';
import { PlayersAuditUpdateComponent } from './update/players-audit-update.component';
import { PlayersAuditDeleteDialogComponent } from './delete/players-audit-delete-dialog.component';
import { PlayersAuditRoutingModule } from './route/players-audit-routing.module';

@NgModule({
  imports: [SharedModule, PlayersAuditRoutingModule],
  declarations: [PlayersAuditComponent, PlayersAuditDetailComponent, PlayersAuditUpdateComponent, PlayersAuditDeleteDialogComponent],
  entryComponents: [PlayersAuditDeleteDialogComponent],
})
export class PlayersAuditModule {}
