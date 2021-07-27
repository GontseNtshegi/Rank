import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayers } from '../players.model';
import { PlayersService } from '../service/players.service';

@Component({
  templateUrl: './players-delete-dialog.component.html',
})
export class PlayersDeleteDialogComponent {
  players?: IPlayers;

  constructor(protected playersService: PlayersService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.playersService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
