jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlayersAuditService } from '../service/players-audit.service';
import { IPlayersAudit, PlayersAudit } from '../players-audit.model';
import { IPlayers } from 'app/entities/players/players.model';
import { PlayersService } from 'app/entities/players/service/players.service';

import { PlayersAuditUpdateComponent } from './players-audit-update.component';

describe('Component Tests', () => {
  describe('PlayersAudit Management Update Component', () => {
    let comp: PlayersAuditUpdateComponent;
    let fixture: ComponentFixture<PlayersAuditUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let playersAuditService: PlayersAuditService;
    let playersService: PlayersService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlayersAuditUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlayersAuditUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayersAuditUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      playersAuditService = TestBed.inject(PlayersAuditService);
      playersService = TestBed.inject(PlayersService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Players query and add missing value', () => {
        const playersAudit: IPlayersAudit = { transactionId: 'CBA' };
        const players: IPlayers = { id: 78903 };
        playersAudit.players = players;

        const playersCollection: IPlayers[] = [{ id: 54572 }];
        spyOn(playersService, 'query').and.returnValue(of(new HttpResponse({ body: playersCollection })));
        const additionalPlayers = [players];
        const expectedCollection: IPlayers[] = [...additionalPlayers, ...playersCollection];
        spyOn(playersService, 'addPlayersToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ playersAudit });
        comp.ngOnInit();

        expect(playersService.query).toHaveBeenCalled();
        expect(playersService.addPlayersToCollectionIfMissing).toHaveBeenCalledWith(playersCollection, ...additionalPlayers);
        expect(comp.playersSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const playersAudit: IPlayersAudit = { transactionId: 'CBA' };
        const players: IPlayers = { id: 39923 };
        playersAudit.players = players;

        activatedRoute.data = of({ playersAudit });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(playersAudit));
        expect(comp.playersSharedCollection).toContain(players);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const playersAudit = { transactionId: 'ABC' };
        spyOn(playersAuditService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ playersAudit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: playersAudit }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(playersAuditService.update).toHaveBeenCalledWith(playersAudit);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const playersAudit = new PlayersAudit();
        spyOn(playersAuditService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ playersAudit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: playersAudit }));
        saveSubject.complete();

        // THEN
        expect(playersAuditService.create).toHaveBeenCalledWith(playersAudit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const playersAudit = { transactionId: 'ABC' };
        spyOn(playersAuditService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ playersAudit });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(playersAuditService.update).toHaveBeenCalledWith(playersAudit);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackPlayersById', () => {
        it('Should return tracked Players primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackPlayersById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
