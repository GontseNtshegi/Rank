import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IPlayersAudit } from '../players-audit.model';

@Component({
  selector: 'jhi-players-audit-detail',
  templateUrl: './players-audit-detail.component.html',
})
export class PlayersAuditDetailComponent implements OnInit {
  playersAudit: IPlayersAudit | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playersAudit }) => {
      this.playersAudit = playersAudit;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
