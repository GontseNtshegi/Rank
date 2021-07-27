import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlayersDetailComponent } from './players-detail.component';

describe('Component Tests', () => {
  describe('Players Management Detail Component', () => {
    let comp: PlayersDetailComponent;
    let fixture: ComponentFixture<PlayersDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PlayersDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ players: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(PlayersDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayersDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load players on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.players).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
