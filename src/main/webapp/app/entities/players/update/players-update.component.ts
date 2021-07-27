import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IPlayers, Players } from '../players.model';
import { PlayersService } from '../service/players.service';

@Component({
  selector: 'jhi-players-update',
  templateUrl: './players-update.component.html',
})
export class PlayersUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    currentBalance: [],
    wageringMoney: [],
    winningMoney: [],
    username: [],
    email: [],
    contact: [],
  });

  constructor(protected playersService: PlayersService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ players }) => {
      this.updateForm(players);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const players = this.createFromForm();
    if (players.id !== undefined) {
      this.subscribeToSaveResponse(this.playersService.update(players));
    } else {
      this.subscribeToSaveResponse(this.playersService.create(players));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPlayers>>): void {
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

  protected updateForm(players: IPlayers): void {
    this.editForm.patchValue({
      id: players.id,
      currentBalance: players.currentBalance,
      wageringMoney: players.wageringMoney,
      winningMoney: players.winningMoney,
      username: players.username,
      email: players.email,
      contact: players.contact,
    });
  }

  protected createFromForm(): IPlayers {
    return {
      ...new Players(),
      id: this.editForm.get(['id'])!.value,
      currentBalance: this.editForm.get(['currentBalance'])!.value,
      wageringMoney: this.editForm.get(['wageringMoney'])!.value,
      winningMoney: this.editForm.get(['winningMoney'])!.value,
      username: this.editForm.get(['username'])!.value,
      email: this.editForm.get(['email'])!.value,
      contact: this.editForm.get(['contact'])!.value,
    };
  }
}
