import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayers } from '../players.model';
import { PlayersService } from '../service/players.service';
import { PlayersDeleteDialogComponent } from '../delete/players-delete-dialog.component';

@Component({
  selector: 'jhi-players',
  templateUrl: './players.component.html',
})
export class PlayersComponent implements OnInit {
  players?: IPlayers[];
  isLoading = false;

  constructor(protected playersService: PlayersService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.playersService.query().subscribe(
      (res: HttpResponse<IPlayers[]>) => {
        this.isLoading = false;
        this.players = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IPlayers): number {
    return item.id!;
  }

  delete(players: IPlayers): void {
    const modalRef = this.modalService.open(PlayersDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.players = players;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
