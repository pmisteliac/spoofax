package org.metaborg.spoofax.core.analysis.constraint;

import org.metaborg.core.MetaborgException;
import org.metaborg.nabl2.ConstraintBuilder;
import org.metaborg.nabl2.TermBuilder;
import org.metaborg.scopegraph.INameResolution;
import org.metaborg.scopegraph.IScopeGraph;
import org.metaborg.scopegraph.ScopeGraphException;
import org.metaborg.scopegraph.impl.NameResolution;
import org.metaborg.scopegraph.impl.ScopeGraph;
import org.metaborg.solver.ISolution;
import org.metaborg.solver.constraints.CTrue;
import org.metaborg.solver.constraints.IConstraint;
import org.spoofax.interpreter.core.Tools;
import org.spoofax.interpreter.terms.IStrategoAppl;
import org.spoofax.interpreter.terms.IStrategoTerm;

public class ResultBuilder {

    private final TermBuilder termBuilder;
    private final ConstraintBuilder constraintBuilder;


    public ResultBuilder() {
        termBuilder = new TermBuilder();
        constraintBuilder = new ConstraintBuilder(termBuilder);
    }

    public InitialResult initialResult(IStrategoTerm term) throws MetaborgException {
        if (!Tools.hasConstructor((IStrategoAppl) term, "InitialResult", 1)) {
            throw new MetaborgException("Wrong format for initial result.");
        }
        final IStrategoTerm analysis = term.getSubterm(0);
        IConstraint constraint = new CTrue();
        for (IStrategoTerm component : analysis) {
            if (Tools.hasConstructor((IStrategoAppl) component, "Constraint", 1)) {
                constraint = constraintBuilder.build(component);
            }
        }
        return new InitialResult(constraint, analysis);
    }

    public UnitResult unitResult(IStrategoTerm term) throws MetaborgException {
        if (!Tools.hasConstructor((IStrategoAppl) term, "UnitResult", 2)) {
            throw new MetaborgException("Wrong format for unit result.");
        }
        final IStrategoTerm ast = term.getSubterm(0);
        final IStrategoTerm analysis = term.getSubterm(1);
        IConstraint constraint = new CTrue();
        for (IStrategoTerm component : analysis) {
            if (Tools.hasConstructor((IStrategoAppl) component, "Constraint", 1)) {
                constraint = constraintBuilder.build(component);
            }
        }
        return new UnitResult(ast, constraint, analysis);
    }

    public FinalResult finalResult(IStrategoTerm term, ISolution solution) throws MetaborgException {
        if (!Tools.hasConstructor((IStrategoAppl) term, "FinalResult", 4)) {
            throw new MetaborgException("Wrong format for final result.");
        }
        IStrategoTerm errors = term.getSubterm(0);
        IStrategoTerm warnings = term.getSubterm(1);
        IStrategoTerm notes = term.getSubterm(2);
        IStrategoTerm analysis = term.getSubterm(3);
        IScopeGraph scopeGraph = null;
        INameResolution nameResolution = null;
        for (IStrategoTerm component : analysis) {
            if (Tools.hasConstructor((IStrategoAppl) component, "ScopeGraph", 1)) {
                scopeGraph = new ScopeGraph(component.getSubterm(0));
            }
            if (Tools.hasConstructor((IStrategoAppl) component, "NameResolution", 1)) {
                try {
                    nameResolution = new NameResolution(component.getSubterm(0));
                } catch (ScopeGraphException e) {
                    throw new MetaborgException("Failed to construct name resolution.", e);
                }
            }
        }
        return new FinalResult(scopeGraph, nameResolution, solution, errors, warnings, notes, analysis);
    }

}
