import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { PlayersAuditDetailComponent } from './players-audit-detail.component';

describe('Component Tests', () => {
  describe('PlayersAudit Management Detail Component', () => {
    let comp: PlayersAuditDetailComponent;
    let fixture: ComponentFixture<PlayersAuditDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [PlayersAuditDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ playersAudit: { transactionId: 'ABC' } }) },
          },
        ],
      })
        .overrideTemplate(PlayersAuditDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(PlayersAuditDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load playersAudit on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.playersAudit).toEqual(jasmine.objectContaining({ transactionId: 'ABC' }));
      });
    });
  });
});
