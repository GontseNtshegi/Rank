import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayersAudit } from '../players-audit.model';
import { PlayersAuditService } from '../service/players-audit.service';

@Component({
  templateUrl: './players-audit-delete-dialog.component.html',
})
export class PlayersAuditDeleteDialogComponent {
  playersAudit?: IPlayersAudit;

  constructor(protected playersAuditService: PlayersAuditService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: string): void {
    this.playersAuditService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
