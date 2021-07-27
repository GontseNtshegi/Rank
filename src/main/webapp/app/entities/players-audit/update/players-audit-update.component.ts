import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IPlayersAudit, PlayersAudit } from '../players-audit.model';
import { PlayersAuditService } from '../service/players-audit.service';
import { IPlayers } from 'app/entities/players/players.model';
import { PlayersService } from 'app/entities/players/service/players.service';

@Component({
  selector: 'jhi-players-audit-update',
  templateUrl: './players-audit-update.component.html',
})
export class PlayersAuditUpdateComponent implements OnInit {
  isSaving = false;

  playersSharedCollection: IPlayers[] = [];

  editForm = this.fb.group({
    eventDate: [],
    operation: [],
    winningMoney: [],
    transactionId: [null, [Validators.required]],
    playerId: [],
    promotion: [],
    players: [],
  });

  constructor(
    protected playersAuditService: PlayersAuditService,
    protected playersService: PlayersService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ playersAudit }) => {
      if (playersAudit.transactionId === undefined) {
        const today = dayjs().startOf('day');
        playersAudit.eventDate = today;
      }

      this.updateForm(playersAudit);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const playersAudit = this.createFromForm();
    if (playersAudit.transactionId !== undefined) {
      this.subscribeToSaveResponse(this.playersAuditService.update(playersAudit));
    } else {
      this.subscribeToSaveResponse(this.playersAuditService.create(playersAudit));
    }
  }

  trackPlayersById(index: number, item: IPlayers): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayersAudit>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(playersAudit: IPlayersAudit): void {
    this.editForm.patchValue({
      eventDate: playersAudit.eventDate ? playersAudit.eventDate.format(DATE_TIME_FORMAT) : null,
      operation: playersAudit.operation,
      winningMoney: playersAudit.winningMoney,
      transactionId: playersAudit.transactionId,
      playerId: playersAudit.playerId,
      promotion: playersAudit.promotion,
      players: playersAudit.players,
    });

    this.playersSharedCollection = this.playersService.addPlayersToCollectionIfMissing(this.playersSharedCollection, playersAudit.players);
  }

  protected loadRelationshipsOptions(): void {
    this.playersService
      .query()
      .pipe(map((res: HttpResponse<IPlayers[]>) => res.body ?? []))
      .pipe(map((players: IPlayers[]) => this.playersService.addPlayersToCollectionIfMissing(players, this.editForm.get('players')!.value)))
      .subscribe((players: IPlayers[]) => (this.playersSharedCollection = players));
  }

  protected createFromForm(): IPlayersAudit {
    return {
      ...new PlayersAudit(),
      eventDate: this.editForm.get(['eventDate'])!.value ? dayjs(this.editForm.get(['eventDate'])!.value, DATE_TIME_FORMAT) : undefined,
      operation: this.editForm.get(['operation'])!.value,
      winningMoney: this.editForm.get(['winningMoney'])!.value,
      transactionId: this.editForm.get(['transactionId'])!.value,
      playerId: this.editForm.get(['playerId'])!.value,
      promotion: this.editForm.get(['promotion'])!.value,
      players: this.editForm.get(['players'])!.value,
    };
  }
}
