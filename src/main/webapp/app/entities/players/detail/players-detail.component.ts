import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayers } from '../players.model';

@Component({
  selector: 'jhi-players-detail',
  templateUrl: './players-detail.component.html',
})
export class PlayersDetailComponent implements OnInit {
  players: IPlayers | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ players }) => {
      this.players = players;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
