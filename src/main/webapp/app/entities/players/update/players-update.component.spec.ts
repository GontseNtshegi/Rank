jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { PlayersService } from '../service/players.service';
import { IPlayers, Players } from '../players.model';

import { PlayersUpdateComponent } from './players-update.component';

describe('Component Tests', () => {
  describe('Players Management Update Component', () => {
    let comp: PlayersUpdateComponent;
    let fixture: ComponentFixture<PlayersUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let playersService: PlayersService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [PlayersUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(PlayersUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(PlayersUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      playersService = TestBed.inject(PlayersService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const players: IPlayers = { id: 456 };

        activatedRoute.data = of({ players });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(players));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const players = { id: 123 };
        spyOn(playersService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ players });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: players }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(playersService.update).toHaveBeenCalledWith(players);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const players = new Players();
        spyOn(playersService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ players });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: players }));
        saveSubject.complete();

        // THEN
        expect(playersService.create).toHaveBeenCalledWith(players);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const players = { id: 123 };
        spyOn(playersService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ players });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(playersService.update).toHaveBeenCalledWith(players);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
