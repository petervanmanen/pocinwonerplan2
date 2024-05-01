import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Actiehouder e2e test', () => {
  const actiehouderPageUrl = '/actiehouder';
  const actiehouderPageUrlPattern = new RegExp('/actiehouder(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const actiehouderSample = {};

  let actiehouder;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/actiehouders+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/actiehouders').as('postEntityRequest');
    cy.intercept('DELETE', '/api/actiehouders/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (actiehouder) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/actiehouders/${actiehouder.id}`,
      }).then(() => {
        actiehouder = undefined;
      });
    }
  });

  it('Actiehouders menu should load Actiehouders page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('actiehouder');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Actiehouder').should('exist');
    cy.url().should('match', actiehouderPageUrlPattern);
  });

  describe('Actiehouder page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(actiehouderPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Actiehouder page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/actiehouder/new$'));
        cy.getEntityCreateUpdateHeading('Actiehouder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', actiehouderPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/actiehouders',
          body: actiehouderSample,
        }).then(({ body }) => {
          actiehouder = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/actiehouders+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [actiehouder],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(actiehouderPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Actiehouder page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('actiehouder');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', actiehouderPageUrlPattern);
      });

      it('edit button click should load edit Actiehouder page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Actiehouder');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', actiehouderPageUrlPattern);
      });

      it('edit button click should load edit Actiehouder page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Actiehouder');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', actiehouderPageUrlPattern);
      });

      it('last delete button click should delete instance of Actiehouder', () => {
        cy.intercept('GET', '/api/actiehouders/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('actiehouder').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', actiehouderPageUrlPattern);

        actiehouder = undefined;
      });
    });
  });

  describe('new Actiehouder page', () => {
    beforeEach(() => {
      cy.visit(`${actiehouderPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Actiehouder');
    });

    it('should create an instance of Actiehouder', () => {
      cy.get(`[data-cy="naam"]`).type('substitute fooey during');
      cy.get(`[data-cy="naam"]`).should('have.value', 'substitute fooey during');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        actiehouder = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', actiehouderPageUrlPattern);
    });
  });
});
