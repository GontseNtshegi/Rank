import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IPlayersAudit } from '../players-audit.model';
import { PlayersAuditService } from '../service/players-audit.service';
import { PlayersAuditDeleteDialogComponent } from '../delete/players-audit-delete-dialog.component';

@Component({
  selector: 'jhi-players-audit',
  templateUrl: './players-audit.component.html',
})
export class PlayersAuditComponent implements OnInit {
  playersAudits?: IPlayersAudit[];
  isLoading = false;

  constructor(protected playersAuditService: PlayersAuditService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.playersAuditService.query().subscribe(
      (res: HttpResponse<IPlayersAudit[]>) => {
        this.isLoading = false;
        this.playersAudits = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackTransactionId(index: number, item: IPlayersAudit): string {
    return item.transactionId!;
  }

  delete(playersAudit: IPlayersAudit): void {
    const modalRef = this.modalService.open(PlayersAuditDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.playersAudit = playersAudit;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
